#! /bin/bash
pid=`ps -ef | grep MyService.jar | grep -v grep | awk '{print $2}'`
kill -9 $pid
