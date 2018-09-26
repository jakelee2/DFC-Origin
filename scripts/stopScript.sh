#!/bin/bash

echo "Start to kill Scripts...\n"
kill `ps aux | grep $1 | grep python | awk '{print $2}'`
echo "Done!!!"
