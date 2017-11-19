package com.example.android.eyebody.management.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageFragment

/**
 * Created by YOON on 2017-11-11
 */
class ConfigManagementFragment : BasePageFragment() {

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

        listview.adapter = ConfigManagementAdapter(context,
                arrayOf(
                        ConfigManagementContent(context.resources.getDrawable(R.drawable.icons8maintenance, null),
                                "Example", "Example for sub text",
                                arrayOf(
                                        SwitchableSubContent("연습하기", "dd",
                                                listOf("false 상태입니다.", "true 상태입니다.")),
                                        SwitchableSubContent("true or false with switch", "ds",
                                                listOf("false 에요", "true 에요")),
                                        DialogableSubContent("1,2,3 중에 선택", null,
                                                listOf("1번째 선택", "2번째 선택", "3번째 선택"))
                                )
                        ),
                        ConfigManagementContent(context.resources.getDrawable(R.drawable.icons8uploadtocloud, null),
                                "백업 설정", "백업에 대한 설정을 바꿀 수 있습니다.",
                                arrayOf(
                                        DialogableSubContent("구글 드라이브 로그인", "Backup_NONE",
                                                listOf("로그아웃 상태", "로그인 상태")),
                                        DialogableSubContent("구글 드라이브 사용자 변경", null, null),

                                        SwitchableSubContent("Wifi 환경에서만 백업", "Backup_Only_Wifi",
                                                listOf("LTE를 이용할 때도 백업합니다.", "LTE를 이용할 때는 백업 하지 않습니다.")),
                                        DialogableSubContent("구글 드라이브 백업하기", null, null),
                                        SwitchableSubContent("자동 백업", "Backup_Auto",
                                                listOf("자동 백업기능을 사용하지 않고 있습니다.", "자동 백업기능이 활성화되었습니다."))
                                )
                        ),
                        ConfigManagementContent(context.resources.getDrawable(R.drawable.icons8plus, null),
                                "보안", "보안에 대한 설정을 바꿀 수 있습니다.",
                                arrayOf(
                                        SwitchableSubContent("지문인식 사용", "Security_Use_Fingerprint",
                                                listOf("지문인식을 사용하지 않습니다.", "지문인식을 사용합니다.")),
                                        DialogableSubContent("비밀번호 찾기", null, null)
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
