package com.example.android.eyebody.management.config

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast

import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageFragment
import com.example.android.eyebody.management.config.subcontent.SwitchableSubContent
import com.example.android.eyebody.management.config.subcontent.callee.content.ConfirmDialog
import com.example.android.eyebody.management.config.subcontent.callee.content.FindPasswordActivity
import com.example.android.eyebody.management.config.subcontent.caller.*

/**
 * Created by YOON on 2017-11-11
 */
class ConfigManagementFragment : BasePageFragment() {

    val TAG = "mydbg_configMngFrag"

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            /* fragment가 visible 해질 때 action을 부여하고 싶은 경우 */
        } else {
            /* fragment가 visible 하지 않지만 load되어 있는 경우 */
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mView = inflater!!.inflate(R.layout.fragment_management_config, container, false)
        val listview = mView.findViewById<ListView>(R.id.listview_config_management)

        listview.adapter = ConfigManagementAdapter(activity,
                arrayListOf(
                        ConfigManagementContent(
                                context.resources.getDrawable(R.drawable.icons8maintenance, null),
                                "Example",
                                "Example for sub text",
                                arrayListOf(
                                        SwitchableSubContent(
                                                "연습하기",
                                                "nullable1",
                                                listOf("false 상태입니다.", "true 상태입니다.")
                                        ),
                                        SwitchableSubContent(
                                                "true or false with switch",
                                                "this is nullable2",
                                                listOf("false 에요", "true 에요")
                                        ),
                                        DialogCallerSubContent(
                                                "1,2,3 중에 선택",
                                                "select 1 or 2 or 3",
                                                false,
                                                listOf("1번째 선택", "2번째 선택", "3번째 선택"),
                                                listOf(null, null, null)
                                        )
                                )
                        ),
                        ConfigManagementContent(
                                context.resources.getDrawable(R.drawable.icons8uploadtocloud, null),
                                "백업 설정",
                                "백업에 대한 설정을 바꿀 수 있습니다.",
                                arrayListOf(
                                        FunctionCallerSubContent(
                                                "구글 드라이브 로그인",
                                                getString(R.string.sharedPreference_GoogleLogin_Status),
                                                false,
                                                listOf("로그아웃 상태", "로그인 상태"),
                                                listOf(
                                                        Async {
                                                            Toast.makeText(context, "로그인 시도", Toast.LENGTH_SHORT).show()
                                                            /*async task something here*/
                                                            setAsyncReturn(1)
                                                        },
                                                        Sync {
                                                            Toast.makeText(context, "로그아웃 시도", Toast.LENGTH_SHORT).show()
                                                            0
                                                        }
                                                )
                                        ),
                                        DialogCallerSubContent(
                                                "구글 드라이브 사용자 변경",
                                                null,
                                                false,
                                                null,
                                                null
                                        ),
                                        SwitchableSubContent(
                                                "Wifi 환경에서만 백업",
                                                getString(R.string.sharedPreference_Backup_Only_Wifi),
                                                listOf("LTE를 이용할 때도 백업합니다.", "LTE를 이용할 때는 백업 하지 않습니다.")
                                        ),
                                        DialogCallerSubContent(
                                                "구글 드라이브 백업하기",
                                                null,
                                                false,
                                                null,
                                                null
                                        ),
                                        SwitchableSubContent(
                                                "자동 백업",
                                                getString(R.string.sharedPreference_Backup_Auto),
                                                listOf("자동 백업기능을 사용하지 않고 있습니다.", "자동 백업기능이 활성화되었습니다.")
                                        )
                                )
                        ),
                        ConfigManagementContent(
                                context.resources.getDrawable(R.drawable.icons8plus, null),
                                "보안",
                                "보안에 대한 설정을 바꿀 수 있습니다.",
                                arrayListOf(
                                        DialogCallerSubContent(
                                                "지문인식 사용",
                                                getString(R.string.sharedPreference_Security_Use_Fingerprint),
                                                true,
                                                listOf("지문인식을 사용하지 않습니다.", "지문인식을 사용합니다."),
                                                listOf(ConfirmDialog.Builder("지문인식을 사용하시겠습니까?", "지문인식이 지원되어야 합니다.")
                                                        .setConfirmCancelMessage("사용함", "사용안함").build(),
                                                        null)
                                        ),
                                        ActivityCallerSubContent(
                                                "비밀번호 찾기",
                                                null,
                                                false,
                                                null,
                                                listOf(FindPasswordActivity::class.java as Class<AppCompatActivity>)
                                        )
                                )
                        )
                ))

        return mView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            pageNumber = arguments.getInt(ARG_PAGE_NUMBER)
        }
    }

    companion object {
        private val ARG_PAGE_NUMBER = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param pn PageNumber (Int)
         * @return A new instance of fragment ConfigManagementFragment.
         */
        fun newInstance(pn: Int): ConfigManagementFragment {
            val fragment = ConfigManagementFragment()
            val args = Bundle()
            args.putInt(ARG_PAGE_NUMBER, pn)
            fragment.arguments = args
            return fragment
        }

    }

}// Required empty public constructor