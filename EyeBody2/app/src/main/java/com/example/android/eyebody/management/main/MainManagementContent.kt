package com.example.android.eyebody.management.main

/**
 * Created by YOON on 2017-11-19
 */

                            /* inInProgress
                            *  진행 중인 거라면 삭제버튼,
                            *  끝난 거라면 휴지통버튼을
                            *  만든다.*/
class MainManagementContent(var isInProgress: Boolean, var startDate: String, var endDate: String, var data: String)