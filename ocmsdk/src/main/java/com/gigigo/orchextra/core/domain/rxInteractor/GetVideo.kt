package com.gigigo.orchextra.core.domain.rxInteractor

import android.content.Context
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository
import gigigo.com.vimeolibs.VimeoInfo
import io.reactivex.Observable
import javax.inject.Inject

class GetVideo @Inject constructor(
    threadExecutor: PriorityScheduler?,
    postExecutionThread: PostExecutionThread?,
    private val ocmRepository: OcmRepository
) : UseCase<VimeoInfo, GetVideo.Params>(
    threadExecutor,
    postExecutionThread
) {

    override fun buildUseCaseObservable(params: Params): Observable<VimeoInfo> {
        return ocmRepository.getVideo(
            params.context, params.forceReload, params.videoId, params.isWifiConnection,
            params?.isFastConnection
        )
    }

    /*
    final boolean isWifiConnection, final boolean isFastConnection
     */
    class Params private constructor(
        val context: Context,
        val forceReload: Boolean,
        val videoId: String,
        val isWifiConnection: Boolean,
        val isFastConnection: Boolean
    ) {
        companion object {
            fun forVideo(
                context: Context,
                forceReload: Boolean,
                videoId: String,
                isWifiConnection: Boolean,
                isFastConnection: Boolean
            ): Params {
                return Params(context, forceReload, videoId, isWifiConnection, isFastConnection)
            }
        }
    }
}