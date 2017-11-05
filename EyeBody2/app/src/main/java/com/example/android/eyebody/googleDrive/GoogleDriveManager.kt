package com.example.android.eyebody.googleDrive

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveFile
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.drive.metadata.SearchableMetadataField
import com.google.android.gms.drive.query.Filter
import com.google.android.gms.drive.query.Filters
import com.google.android.gms.drive.query.Query
import com.google.android.gms.drive.query.SearchableField
import java.io.*

/**
 * @param context 권한을 얻기 위해.
 * @param activity 전송실패 등의 에러가 날 경우 해당 Activity에 Dialog를 띄움.
 */
open class GoogleDriveManager(val context: Context, val activity: Activity) : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // TODO wifi 모드 / data 모드
    private val TAG = "mydbg_googleDrive"

    /**
     * local : 절대경로 + 파일이름
     * server : 파일이름만 존재
     */
    data class FileTransferData(val fileName: String, val fileLocale: Int)

    companion object {
        val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        val REQUEST_CODE_RESOLUTION = 3
        val NETWORK_MODE_WIFI = 1
        val NETWORK_MODE_DATA = 2
        val FILE_LOCATION_SERVER = 1
        val FILE_LOCATION_LOCAL = -1
    }

    var mGoogleApiClient: GoogleApiClient? = null
    var mIntentSender: IntentSender? = null
    var networkStatus = NETWORK_MODE_WIFI


    init {
        // TODO ----- if needed, check permission
        // context.checkPermission("???", 1, 1)
    }

    /**
     * 로그인을 요청합니다.
     * 로그인이 되어 있는 경우 : 무시
     * 설정된 계정이 없는 경우 : 계정 선택 후 로그인 요청
     * @see checkConnection
     */
    fun signIn() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(context)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE).addScope(Drive.SCOPE_APPFOLDER)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
        }
        if (mGoogleApiClient!!.isConnected || mGoogleApiClient!!.isConnecting)
        else
            mGoogleApiClient?.connect() ?: Log.e(TAG, "mGoogleApiClient build null error")
    }

    /**
     * 로그아웃을 요청합니다.
     * @see checkConnection
     */
    fun signOut() {
        mGoogleApiClient?.disconnect()
        onConnectionStatusChanged() //로그인할때는 알아서 리스너가 있음 signout은 없어서 임의로 UI컨트롤을 위해서 작성
    }

    /**
     * authorized 된 계정을 dis authorize 시키고
     * 새로운 계정으로 로그인하게 합니다.
     */
    fun changeAccount() {
        mGoogleApiClient?.clearDefaultAccountAndReconnect()
    }

    /**
     * 연결이 되었는지 반환합니다.
     * @return false : 연결 중이거나 연결이 되어있지 않은경우
     */
    fun checkConnection(): Boolean {
        val connection = mGoogleApiClient?.isConnected ?: false
        Log.i(TAG, "현재 연결상태 : $connection")
        return connection
    }

    /**
     * 주의 : UI 쓰레드에서 실행할 수 없음.
     * Local과 Server의 데이터를 비교한 뒤
     * Local에 없는 ServerFile을 강제로 모두 업로드함.
     * @see getDataForSync
     * @see multipleFileTransfer
     * @return Pair(성공한 요청, 전체 요청) : drive api에 요청한 정보를 반환
     */
    fun saveAllFile(localFiles: Array<String>): Pair<Int, Int> {

        // 싱크 안 맞는 것 중 localfile 만 골라서 싱크 강제로 맞춤.
        val fileTransferDataArray = getDataForSync(localFiles)
                ?.filter { it.fileLocale == FILE_LOCATION_LOCAL }
                ?.toTypedArray()

        if (fileTransferDataArray == null || fileTransferDataArray.isEmpty())
            return Pair(0, 0)

        return multipleFileTransfer(fileTransferDataArray)
    }

    /**
     * 주의 : UI 쓰레드에서 실행할 수 없음.
     * Local과 Server의 데이터를 비교한 뒤
     * Server에 없는 LocalFile을 강제로 모두 업로드함.
     * @see getDataForSync
     * @see multipleFileTransfer
     * @return Pair(성공한 요청, 전체 요청) : drive api에 요청한 정보를 반환
     */
    fun loadAllFile(localFiles: Array<String>): Pair<Int, Int> {

        // 싱크 안 맞는 것 중 serverFile 만 골라서 싱크 강제로 맞춤.
        val fileTransferDataArray = getDataForSync(localFiles)
                ?.filter { it.fileLocale == FILE_LOCATION_SERVER }
                ?.toTypedArray()

        if (fileTransferDataArray == null || fileTransferDataArray.isEmpty())
            return Pair(0, 0)
        return multipleFileTransfer(fileTransferDataArray)
    }

    fun getDataForSync(localFiles: Array<String>): Array<FileTransferData>? {
        // TODO ----- array of filetransferData 를 모두 mutableMap 으로 바꾸는 것이 효율적일 것 같다.

        val avoidSame = mutableMapOf<String, Int>()

        // 서버 데이터 수신에 실패할 경우 null반환
        val serverFileTransferDataArray = getServerFileList()
        if (serverFileTransferDataArray != null) {
            // 서버 데이터를 맵에 넣음
            for (f in serverFileTransferDataArray)
                avoidSame.put(f.fileName, FILE_LOCATION_SERVER)
        }

        // 로컬 데이터를 맵에 넣음 (중복일 경우 삭제)
        for (f in localFiles) {
            val fileName = f.split("/").last()
            if (avoidSame.containsKey(fileName))
                avoidSame.remove(fileName)
            else
                avoidSame.put(f, FILE_LOCATION_LOCAL)
        }
        // map을 array로 변환
        var fileTransferDataArray = arrayOf<FileTransferData>()
        for (f in avoidSame) {
            fileTransferDataArray = fileTransferDataArray.plus(FileTransferData(f.key, f.value))
        }

        Log.d(TAG, "getDataForSync (serverFile + requestFile) size : ${fileTransferDataArray.size}")

        return fileTransferDataArray
    }

    private fun getServerFileList(): Array<FileTransferData>? {

        // Drive 접근
        val newDriveResult = Drive.DriveApi.newDriveContents(mGoogleApiClient).await()
        if (!newDriveResult.status.isSuccess) {
            Log.d(TAG, "실패 : newDriveResult / getServerFileList : ${newDriveResult.status.statusMessage}")
            return null
        }

        var fileTransferDataArray = arrayOf<FileTransferData>()

        // Drive 를 online에서만 하게 강제하고 싱크를 맞춤.
        // TODO 1분에 1번만 가능.. 엄밀히 5분에 5번..
        val sync = Drive.DriveApi.requestSync(mGoogleApiClient).await()
        if (sync.status.isSuccess) {
            Log.d(TAG, "success to sync")

            // 모든 파일들을 요청.
            // TODO 앱폴더 비우기
            val query = Query.Builder().
                    addFilter(Filters.and(
                            Filters.eq(SearchableField.TRASHED, false))
                    ).build()
            val filesResult = Drive.DriveApi.query(mGoogleApiClient, query).await()
            if (filesResult.status.isSuccess) {
                Log.d(TAG, "파일 개수 : ${filesResult.metadataBuffer.count}")
                for (i in filesResult.metadataBuffer) {
                    Log.d(TAG, "\tmetadata : title[${i.title}]      date[${i.createdDate}]      isinAppfolder[${i.isInAppFolder}]")
                    if (!i.isInAppFolder)
                        fileTransferDataArray = (fileTransferDataArray).plus(FileTransferData(i.title, FILE_LOCATION_SERVER))
                }
            }
            filesResult.metadataBuffer.release()
        } else {
            if (sync.statusCode == CommonStatusCodes.NETWORK_ERROR) // 7
                Toast.makeText(activity, "인터넷이 연결되어있지 않습니다.", Toast.LENGTH_LONG).show()
            else {
                Log.d(TAG, "failure : ${sync.statusMessage}\n" +
                        "and status code is ${sync.statusCode}.\n" +
                        "see > https://developers.google.com/android/reference/com/google/android/gms/common/api/CommonStatusCodes.html#NETWORK_ERROR")
            }
            return null
        }

        return fileTransferDataArray
    }

    /**
     * 여러 개의 파일을 전송(업로드/다운로드) 함
     * fileTransferDataArray = {파일 이름, 장소}의 배열
     * 장소를 확인하고 local은 서버로 server는 로컬로 전송
     * @see getDataForSync
     * @return Pair of (success of request to google, Total) : 성공한 요청은 데이터가 원활하지 않을 경우 전송이 중단될 수 있으며 다시 연결될 경우 이어서 전송합니다.
     */
    fun multipleFileTransfer(fileTransferDataArray: Array<FileTransferData>): Pair<Int, Int> {

        var successCount = 0
        var totalCount = 0

        // upload와 download는 내부에서 전송용 스택이 쌓임.
        for (goods in fileTransferDataArray)
            when (goods.fileLocale) {
                FILE_LOCATION_LOCAL -> {
                    successCount += upload(goods.fileName)  //fileName 은 절대경로 + 파일 이름
                    totalCount++
                }
                FILE_LOCATION_SERVER -> {
                    successCount += download(goods.fileName)  //fileName 은 파일 이름
                    totalCount++
                }
            }

        return Pair(successCount, totalCount) //성공하면 true 실패하면 false
    }

    private fun upload(photoURI: String): Int {


        if (!checkConnection())
            return 0

        //sync 방법
        val newDriveResult = Drive.DriveApi.newDriveContents(mGoogleApiClient).await()
        if (!newDriveResult.status.isSuccess) {
            Log.d(TAG, "실패 : newDriveResult / upload : ${newDriveResult.status.statusMessage}")
            return 0
        }

        // TODO 파일이름이 올바르지 않을 경우 (없는 파일을 전송하고자 할 때 등등) 에러에 대한 예외처리 in outputstream
        val outputStream = newDriveResult.driveContents.outputStream
        val bitmapStream = ByteArrayOutputStream()
        try {
            BitmapFactory
                    .decodeStream(FileInputStream(File(photoURI)))
                    .compress(Bitmap.CompressFormat.PNG, 100, bitmapStream)
            outputStream.write(bitmapStream.toByteArray())
        } catch (exception: IOException) {
            Log.d(TAG, "IOException : bitmapStream -> driveContents.outputStream\n" +
                    "              ${exception.localizedMessage}")
            return 0
        }

        val metadataChangeSet = MetadataChangeSet.Builder()
                .setMimeType("application/eyebody")
                .setTitle(photoURI.split("/").last())
                .setDescription("Encrypted eyebody backup file")
                .build()

        // 앱폴더 / 위에서 scope 를 appfolder로 해야함. : user나 타 앱이 액세스 불가능
        // Drive.DriveApi.getAppFolder(mGoogleApiClient)
        // Drive id 가 매번 같은데 이게 같은 앱폴더라 같은 세션같은 개념인지 먼지 잘 모르겠음.
        // TODO ----- 루트폴더로 일단 다 해보고 잘되면 앱폴더로 해서 테스트하면 될 듯.

        // 루트폴더 / 구글 드라이브 : user가 액세스 가능함.
        val createFileAtRootResult = Drive.DriveApi.getRootFolder(mGoogleApiClient)
                .createFile(mGoogleApiClient, metadataChangeSet, newDriveResult.driveContents)
                .await()
        if (!createFileAtRootResult.status.isSuccess) {
            Log.d(TAG, "실패 : createFileAtRootResult / upload : ${createFileAtRootResult.status.statusMessage}")
            return 0
        }
        Log.d(TAG, "Created a file with content : id = ${createFileAtRootResult.driveFile.driveId}")

        /*//async 방법
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(
                        ResultCallback
                        { result ->

                            if (!result.status.isSuccess) {
                                Log.d(TAG, "create newDriveContents 실패")
                                return@ResultCallback
                            }
                            Log.i(TAG, "driveContents를 만들었기 때문에 데이터를 구글에 쓸 수 있습니다. status : ${result.status}")

                            val outputStream = result.driveContents.outputStream
                            val bitmapStream = ByteArrayOutputStream()
                            BitmapFactory
                                    .decodeStream(FileInputStream(File(photoURI)))
                                    .compress(Bitmap.CompressFormat.PNG, 100, bitmapStream)
                            try {
                                outputStream.write(bitmapStream.toByteArray())
                            } catch (exception: IOException) {
                                Log.d(TAG, "IOException : bitmapStream -> driveContents.outputStream")
                                exception.printStackTrace()
                            }

                            object : Thread() {
                                override fun run() {
                                    val metadataChangeSet = MetadataChangeSet.Builder()
                                            .setMimeType("application/eyebody")
                                            .setTitle(photoURI.split("/").last())
                                            .setDescription("Encrypted eyebody backup file")
                                            .build()


                                    // 앱폴더 / 위에서 scope 를 appfolder로 해야함. : user나 타 앱이 액세스 불가능
                                    // Drive.DriveApi.getAppFolder(mGoogleApiClient)
                                    // Drive id 가 매번 같은데 이게 같은 앱폴더라 같은 세션같은 개념인지 먼지 잘 모르겠음.
                                    // TODO ----- 루트폴더로 일단 다 해보고 잘되면 앱폴더로 해서 테스트하면 될 듯.

                                    // 루트폴더 / 구글 드라이브 : user가 액세스 가능함.
                                    Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                            .createFile(mGoogleApiClient, metadataChangeSet, result.driveContents)
                                            .setResultCallback(
                                                    ResultCallback { result ->
                                                        if (!result.status.isSuccess) {
                                                            Log.d(TAG, "create newDriveContents 실패 - createfile에서")
                                                            return@ResultCallback
                                                        }
                                                        Log.d(TAG, "Created a file with content: ${result.driveFile.driveId}")
                                                    }
                                            )
                                }
                            }.start()
                        }
                )*/
        return 1
    }

    private fun download(photoURI: String): Int {
        if (!checkConnection())
            return 0

        val newDriveResult = Drive.DriveApi.newDriveContents(mGoogleApiClient).await()
        if (!newDriveResult.status.isSuccess) {
            Log.d(TAG, "실패 : newDriveResult / download : ${newDriveResult.status.statusMessage}")
            return 0
        }

        // TODO : Drive Api 로 단일파일 다운로드
        val query = Query.Builder().
                addFilter(Filters.and(
                        Filters.eq(SearchableField.TRASHED, false))
                ).build()
        val queryResult = Drive.DriveApi.query(mGoogleApiClient, query).await()
        if (!queryResult.status.isSuccess) {
            Log.d(TAG, "실패 : queryResult / download : ${queryResult.status.statusMessage}")
            return 0
        }
        for (metadata in queryResult.metadataBuffer) {
            if (metadata.title == photoURI && !metadata.isInAppFolder) {
                val data = metadata.driveId.asDriveFile().open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null/*download progress listner~~ */).await()
                if (!data.status.isSuccess) {
                    Log.d(TAG, "실패 : driveFile().open / download : ${data.status.statusMessage}")
                    return 0
                }

                val imageByte = data.driveContents.inputStream.readBytes()
                Log.d(TAG, "read Contents Byte Size : ${imageByte.size}\n" +
                        "and save to [${activity.getExternalFilesDir(null)}/gallery_body/$photoURI]")

                context.openFileOutput(photoURI, Context.MODE_APPEND)
                for (f in context.fileList()) {
                    Log.d(TAG,f)
                }
                /*try {

                    val file = File("${activity.getExternalFilesDir(null)}/gallery_body/$photoURI")
                    val fos = FileOutputStream(file)
                    fos.write(imageByte)
                    fos.flush()
                    fos.close()
                    Log.d(TAG,"save success")
                }catch(ioe : IOException){
                    ioe.printStackTrace()
                }*/
                /*val root = Environment.getExternalStorageState().toString()
                val myDir = File(root+"/gallery_body/$photoURI")
                myDir.mkdirs()
                val file = File(myDir,photoURI)
                if (file.exists())
                    file.delete()
                try {
                    val out = FileOutputStream(file)
                    out.write(imageByte)
                    out.flush()
                    out.close()
                } catch (e : Exception) {
                    e.printStackTrace()
                }*/


            }
        }

        return 1
    }


    /**
     * override 하기 위해 선언
     */
    open fun onConnectionStatusChanged() {

    }

    override fun onConnected(connectionHint: Bundle?) {
        onConnectionStatusChanged()
        Log.d(TAG, "구글드라이브 연결 콜백 (정상접속)")
    }

    override fun onConnectionSuspended(cause: Int) {
        onConnectionStatusChanged()
        Log.d(TAG, "구글드라이브 연결 콜백 (정상종료) : $cause")
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        onConnectionStatusChanged()
        /*비정상적 종료에 대해 다시 시도할지, 또는 어떻게 할 지에 대해 작성하는 지역
        일반적으로는 에러 로그를 찍고, 해결가능한 실패에 대해서는 다시 시도한다.
        해당 연결이 중첩되서 발생할 수 있는가? 만약 그런다면 boolean 하나 만들어서 hasResoulution이 아니더라도 건너뛰는 걸 만들어야 할 것 같다.*/
        Log.d(TAG, "connectionFailed\n구글드라이브 에러종료 : $result\n" +
                "${result.errorCode} : ${result.errorMessage}")

        // 해결되지 않은 오류에 대한 후 처리
        if (!result.hasResolution()) {
            // 해결되지 않는 에러에 대해 다이얼로그를 띄워 사용자에게 보여준다.
            // GoogleApiAvailability.getInstance().getErrorDialog(activity, result.errorCode, 0).show()

            // 다이얼로그 대신 로그 띄움.
            Log.d("TAG", GoogleApiAvailability.getInstance().getErrorString(result.errorCode))
            return
        }

        // 해결된 오류에 대한 후 처리
        //   -> 보통 authorization을 다시 시도하면서 알아서 해결함.
        // 안되면 try catch로 에러 호출하고 앱 사망 방지
        try {
            result.startResolutionForResult(activity /*해결된 오류에 대해 다시 시도할 액티비티*/, REQUEST_CODE_RESOLUTION /*onActivityResult 로 보내는 request code*/)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "start resolution for result exception", e)
        }
    }
}