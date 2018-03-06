<?php
	$storage_folder="upload"; //remember to set right on 777 on this folder
	$storage_folder="/".$storage_folder."/";

	header("Access-Control-Allow-Origin: *");
	header("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept");
	//1) retreive and parse JSON data
	$str_json = file_get_contents('php://input');
    if(is_null($str_json) || $str_json==""){
		die("json is empty");
	}
	$jsonobject = json_decode($str_json);
	$ext = $jsonobject->{'ext'};//access data field in json object
	if(is_null($ext) || $ext==""){
		die("ext is empty");
	}
	//generate a filename
	$rndbytes = openssl_random_pseudo_bytes(16);
	$filename = bin2hex($rndbytes).".".$ext;
	//create the file and fill it with data
	$data = $jsonobject->{'data'};
	if(is_null($data) || $data==""){
		die("ext is empty");
	}
	$data = base64_decode($data);
	$image = imagecreatefromstring($data);
	if ($image == false) {
		die("unable to create image from string");
	}
	imagejpeg($image, ".".$storage_folder.$filename);
	//return the file URL
	$actual_link = "https://".$_SERVER['HTTP_HOST'].dirname($_SERVER['PHP_SELF']);
	echo $actual_link.$storage_folder.$filename;

?>
 