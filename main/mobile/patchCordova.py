import os
path = "./cordova.js";
with open(path, "rt") as fin:
    with open(path+".tmp", "wt") as fout:
        for line in fin:
            fout.write(line.replace('xhr.open("get", "/config.xml", true);', 'xhr.open("get", "config.xml", true);'))

os.remove(path);
os.rename(path+".tmp",path)
