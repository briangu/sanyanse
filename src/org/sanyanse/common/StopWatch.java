package org.sanyanse.common;

public class StopWatch
{
  long _startTime;
  long _endTime;
  long _duration = 0;

  public void start() {
    _startTime = System.nanoTime();
  }

  public void stop() {
    _endTime = System.nanoTime();
    _duration = _endTime - _startTime;
  }

  public long getEndTime() {
    return _endTime;
  }

  public long getStartTime() {
    return _startTime;
  }

  public long getDuration() {
    return _duration;
  }
}
