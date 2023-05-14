#!/bin/bash

BASE_DIR=`cd $(dirname $0)/..; pwd`

JOB_ID=$1;

PIDFILE=${BASE_DIR}/pids/${JOB_ID}.pid
echo $PIDFILE

if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
	echo "job is not running..."
else
	echo "stopping ${JOB_ID}..."
	PID="$(cat "$PIDFILE")"
	kill -9 $PID
	rm "$PIDFILE"
	echo "...${JOB_ID} stopped"
fi
