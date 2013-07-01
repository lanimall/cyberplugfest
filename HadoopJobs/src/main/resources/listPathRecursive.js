var paths=fsh.lsr(basepath);
var arrPaths=paths.toArray();
for(var i=0; i < arrPaths.length; i++){
	if(arrPaths[i].isDir()){
		if(folderInputPaths != "")
			folderInputPaths += ",";
		folderInputPaths += arrPaths[i].getPath();
	}
}

cfg.set("map.input.dir",folderInputPaths);