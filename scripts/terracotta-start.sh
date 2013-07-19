#! /bin/bash
# 
# This script starts the full cluster in the right order (make sure the right nodes are active and passive, determined by the start order)
#

export JAVA_OPTS="-server -Xms1G -Xmx1G -XX:+UseParallelOldGC -XX:+UseCompressedOops"
export TERRACOTTA_HOME="/Applications/terracotta/bigmemory-max-4.0.2"
nohup "$TERRACOTTA_HOME/server/bin/start-tc-server.sh" -f "configs/tc-config.xml" -n "Server1" > $HOME/terracotta-nohup.log &