<?php

function getFile($file){
    if (file_exists($file)) {
        header('Content-Description: File Transfer');
        header('Content-Type: application/octet-stream');
        header('Content-Disposition: attachment; filename="'.basename($file).'"');
        header('Expires: 0');
        header('Cache-Control: must-revalidate');
        header('Pragma: public');
        header('Content-Length: ' . filesize($file));
        readfile($file);
        exit;
    }
}

$basepath = "/srv/www/minecraft.dzwxgames.com/minecraft";
$realBase = realpath($basepath);

$userpath = $basepath . DIRECTORY_SEPARATOR . $_GET['file'];
$realUserPath = realpath($userpath);

if ($realUserPath === false || strpos($realUserPath, $realBase) !== 0) {
    echo "Directory Traversal!";
} else {
    getFile($userpath);
}
?>