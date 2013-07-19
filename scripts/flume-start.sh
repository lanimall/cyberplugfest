export FLUME_INSTALL=/Applications/flume/home
$FLUME_INSTALL/bin/flume-ng agent --conf conf --conf-file $FLUME_INSTALL/conf/flume.conf --name agent -Dflume.root.logger=INFO,console