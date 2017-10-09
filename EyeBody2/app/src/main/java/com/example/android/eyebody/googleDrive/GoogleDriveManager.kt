package com.example.android.eyebody.googleDrive

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive

/**
 * Created by Yoon on 2017-10-07.
 */


class GoogleDriveManager(context: Context) {

    init {
        //TODO checking permission
        context.checkPermission("???", 1, 1)

        val mGoogleApiClient = GoogleApiClient.Builder(context)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(GoogleDriveConnection())
                .addOnConnectionFailedListener(GoogleDriveConnection())
                .build()
    }


    companion object {
        val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        val REQUEST_CODE_RESOLUTION = 3
        val WIFI_MODE = 1
        val DATA_MODE = 2
        val SIGN_IN = 1
        val SIGN_OUT = 2
    }


    var view: View? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var statusText: TextView? = null
    var statusWifiOrData = WIFI_MODE
    var statusSignInOrOut = SIGN_OUT


    data class FileTransferData(val fileName: String, val fileLocale: Int)

    val SERVER_FILE = 1
    val LOCAL_FILE = -1


    /**
     * 구글 로그인을 요청합니다.
     * 로그인이 되어 있는 경우 : 무시
     * 설정된 계정이 없는 경우 : 계정 선택 후 로그인 요청
     * @return Boolean = {로그인 성공(true) or 실패(false)}
     */
    fun signIn(): Boolean {
        // TODO 구글 로그인 하게 하기
        statusSignInOrOut = SIGN_IN
        return true
    }

    fun signOut() {
        // TODO 구글 로그아웃 하게 하기
        statusSignInOrOut = SIGN_OUT
    }

    /**
     * Local과 Server의 데이터를 비교한 뒤
     * Local에 없는 ServerFile을 강제로 모두 업로드함.
     * @see getSyncData
     * @see enforceFileSync
     * @return Boolean = {비교 한 번 더 해서 Server에 있는게 Local에 있으면 true}
     * 2. 로컬과 서버
     */
    fun saveAllFile(): Boolean {

        // 싱크 안 맞는 것 중 serverfile 만 골라서 싱크 강제로 맞춤.
        val fileTransferDataArray = getSyncData().filter{it.fileLocale==SERVER_FILE}.toTypedArray()
        enforceFileSync(fileTransferDataArray)

        // 싱크 중 serverfile 싱크미스가 있다면 실패
        if (getSyncData().filter{it.fileLocale==SERVER_FILE}.isNotEmpty())
            // TODO 이 부분 제대로 짠 지 모르겠음
            return false
        return true
    }

    fun loadAllFile() : Boolean {

        // 싱크 안 맞는 것 중 localfile 만 골라서 싱크 강제로 맞춤.
        val fileTransferDataArray = getSyncData().filter{it.fileLocale==SERVER_FILE}.toTypedArray()
        enforceFileSync(fileTransferDataArray)

        // 싱크 중 localfile 싱크미스가 있다면 실패
        if (getSyncData().filter{it.fileLocale==LOCAL_FILE}.isNotEmpty())
        // TODO 이 부분 제대로 짠 지 모르겠음
            return false
        return true
    }

    fun getSyncData(): Array<FileTransferData> {

        val fileNames = arrayOf(
                "171009~~.eyebody",
                "171010~~.eyebody"
                /* ...... */)
        val fileLocales = intArrayOf(
                SERVER_FILE,
                LOCAL_FILE
                /* ...... */)
        /*
        TODO 구글드라이브 싱크 구현
        GoogleDrive에서 싱크한 뒤
        위와 같은 데이터를 얻게 되면 이를 반환함.
         */

        // 두 array를 전송용 데이터로 만들고 반환
        val fileTransferDataArray = arrayOf<FileTransferData>()
        for (data in 1..fileNames.size)
            fileTransferDataArray[data] = FileTransferData(fileNames[data], fileLocales[data])

        return fileTransferDataArray
    }

    fun enforceFileSync(fileTransferDataArray: Array<FileTransferData>): Boolean {

        /*
        TODO 구글드라이브 업로드 구현
        싱크로 부터 얻거나 임의로 만든 전송용 string 데이터로 서버에 저장시키는 것.
         */
        //fileTransferDataArray

        return true //성공하면 true 실패하면 false
    }




    /*fun status(){
        if(mGoogleApiClient?.isConnected ?: false){
            statusText?.text = "연결 된 상태"
        } else if (mGoogleApiClient?.isConnecting ?: false) {
            statusText?.text = "연결 중"
        } else {
            statusText?.text = "연결 안 됨"
        }
    }*/

}