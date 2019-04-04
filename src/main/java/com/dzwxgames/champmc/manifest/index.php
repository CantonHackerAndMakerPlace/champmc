<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
    /*
        Read contents of minecraft folder

        Create JSON
        "forgeversion" : contents of version.json
        "filelist": <file location> : <file hash>
        "librarylist": read contents of lib.json
    */

    function dirToArray($dir) { 
   
        $result = array(); 
     
        $cdir = array_diff(scandir($dir), array('..', '.')); 
        foreach ($cdir as $key => $value) 
        { 
            if (is_dir($dir . DIRECTORY_SEPARATOR . $value)) 
            { 
                $dirlist = dirToArray($dir . DIRECTORY_SEPARATOR . $value);
                foreach ($dirlist as &$svalue) {
                    $result[] = $value . DIRECTORY_SEPARATOR . $svalue;
                }
            } 
            else 
            { 
               $result[] = $value; 
            } 
        } 
        
        return $result; 
    }


    $directory = '/srv/www/minecraft.dzwxgames.com/minecraft';

    $listoffiles = dirToArray($directory);
    $hashfilelist = array();
    foreach ($listoffiles as &$file) {
        $hashfilelist[$file] = md5_file( $directory . "/" .$file);
    }
    $output = array();

    //"forgeversion" : contents of version
    $output["forgeversion"] = file_get_contents('version.txt');
    //"filelist": <file location> : <file hash>
    $output["filelist"] = $hashfilelist;
    //"librarylist": read contents of librarys.json
    $output["librarylist"] = json_decode(file_get_contents('librarys.json'));
    echo json_encode($output);
    //print_r($files1);
?>