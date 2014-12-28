#!/bin/bash

java -jar signapk.jar platform.x509.pem platform.pk8 ./bin/GalleryActivity.apk ./bin/GalleryActivity2.apk
#java -jar signapk.jar releasekey.x509.pem releasekey.pk8 ./bin/MMS.apk ./bin/MMS2.apk

