function GetTRKCode2(strBarCode)
{
    var strRet = strBarCode;
    if (strBarCode.length == 34) {
        strRet = strBarCode.substr(22, 12);
    }
    else if (strBarCode.length == 32) {
        strRet = strBarCode.substr(16, 12);
    }
    else if (strBarCode.length == 28) {
        strRet = strBarCode.substr(4, 9);
    }
    else if (strBarCode.length == 12) {
        strRet = strBarCode;
    }
    else if (strBarCode.length == 9) {
        strRet = strBarCode;
    }
    return strRet;
};