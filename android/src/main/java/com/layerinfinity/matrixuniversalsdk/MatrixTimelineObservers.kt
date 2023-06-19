package com.layerinfinity.matrixuniversalsdk

import org.matrix.android.sdk.api.session.room.timeline.Timeline
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent

class MatrixTimelineObservers(private val eventEmitter: RNEventEmitter) : Timeline.Listener {
  companion object {
    private const val TAG = "MatrixTimelineObservers"
  }

  override fun onNewTimelineEvents(eventIds: List<String>) {
    super.onNewTimelineEvents(eventIds)
  }

  override fun onStateUpdated(direction: Timeline.Direction, state: Timeline.PaginationState) {
    super.onStateUpdated(direction, state)
  }

  override fun onTimelineFailure(throwable: Throwable) {
    super.onTimelineFailure(throwable)
  }

  override fun onTimelineUpdated(snapshot: List<TimelineEvent>) {
    super.onTimelineUpdated(snapshot)
  }
}
