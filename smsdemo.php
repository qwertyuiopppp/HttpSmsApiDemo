<?php 
/* @param $jsonStr 发送的json字符串
 * @return array
 */
function http_post($url, $paramStr)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $paramStr);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/x-www-form-urlencoded;charset=utf-8;',
            'Content-Length: ' . strlen($paramStr)
        )
    );
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    return array($httpCode, $response);
}

function http_get($url)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    //将 curl_exec()获取的信息以文件流的形式返回，而不是直接输出。
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    // 在启用 CURLOPT_RETURNTRANSFER 时候将获取数据返回
    curl_setopt($ch, CURLOPT_BINARYTRANSFER, true) ;
    // CURLINFO_HEADER_OUT选项可以拿到请求头信息
    curl_setopt($ch, CURLINFO_HEADER_OUT, true);
    // 不验证SSL
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
    // curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    //         'Content-Type: application/x-www-form-urlencoded',
    //         'Content-Length: ' . strlen($paramStr)
    //     )
    // );
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    return array($httpCode, $response);
}

function getMillisecond() {
        list($t1, $t2) = explode(' ', microtime());
        return (float)sprintf('%.0f',(floatval($t1)+floatval($t2))*1000);
}

$host = 'http://127.0.0.1:5000'; 
$userid = '100138';
$appkey = '123456';


// 发送接口
function SendSms($mobile, $code)
{
    global $host;
    global $userid;
    global $appkey;
    $ts = getMillisecond();
    $sign=md5($userid.$ts.$appkey);
    
	$mobile = $mobile;
	$msgcontent='【签名】您的验证码是'.$code.'，60秒内有效。如非本人操作，请忽略本条短信。';
	$url = $host . "/api/sms/mt";
	$paramStr = "userid=".$userid."&ts=".$ts."&sign=".$sign."&mobile=".$mobile."&content=".urlencode($msgcontent);
    list($returnCode, $returnContent) = http_post($url, $paramStr);
	echo $returnCode;
    echo $returnContent;
    $data = json_decode($returnContent, true);
    return $data['code'];
}

//余额查询接口
function GetBalance()
{
    global $host;
    global $userid;
    global $appkey;
    $ts = getMillisecond();
    $sign=md5($userid.$ts.$appkey);
    $balanceParamStr = "userid=".$userid."&ts=".$ts."&sign=".$sign;
    $balanceUrl = $host . "/api/sms/balance" ."?". $balanceParamStr;
    list($returnCode, $returnContent) = http_get($balanceUrl);
    echo $returnCode;
    echo $returnContent;
}

//报告查询接口
function GetReport()
{
    global $host;
    global $userid;
    global $appkey;
    $ts = getMillisecond();
    $sign=md5($userid.$ts.$appkey);
    $reportParamStr = "userid=".$userid."&ts=".$ts."&sign=".$sign;
    $reportUrl = $host . "/api/sms/report" . "?" . $reportParamStr;
    list($returnCode, $returnContent) = http_get($reportUrl);
    echo $returnCode;
    echo $returnContent;
}

//上行短信查询接口
function GetMoSms()
{
    global $host;
    global $userid;
    global $appkey;
    $ts = getMillisecond();
    $sign=md5($userid.$ts.$appkey);
    $reportParamStr = "userid=".$userid."&ts=".$ts."&sign=".$sign;
    $reportUrl = $host . "/api/sms/mo" . "?" . $reportParamStr;
    list($returnCode, $returnContent) = http_get($reportUrl);
    echo $returnCode;
    echo $returnContent;
}

//调用方法
echo sendSms('13800138000','5637');
//echo GetBalance();
//echo GetReport();
//echo GetMoSms();


?>
