//1
var myClientID = "73434868sssdfd";
let connection = new signalR.HubConnectionBuilder().withUrl("/WVResultHub").build();

//2
function getConnected() {
    connection.start().then(function () {
        //document.getElementById('connectAlertDiv').style.display = "block";
        //document.getElementById('btnConnnect').style.display = "none";       
        TestConnection();
        JoinRoom(signalR_Id)
    }).catch(function (err) {
        return console.error(err.toString());
    });
}
//3
function TestConnection() {
    connection.invoke("NotifyConnection").catch(function (err) {
        return console.error(err.toString());
    });
}
function JoinRoom(signalR_Id) {
       myClientID = signalR_Id;
        connection.invoke("JoinRoom", signalR_Id).catch(function (err) {
        return console.error(err.toString());
    });
}


//4
connection.on("TestBrodcasting", function (time) {
    document.getElementById('broadcastDiv').innerHTML = time;
    document.getElementById('broadcastDiv').style.display = "block";
});

connection.on("onFileChange", function (filedetails) {


    document.getElementById("MS_UR").innerText = filedetails.name;
    document.getElementById("MS_US").innerText = filedetails.changeType;

});


function runVoice() {
    document.getElementById('audio').play();
}
function stopVoice() {
    document.getElementById('audio').pause();
}



//var dev1 = { devId=1 };
//var dev2 = { devId=2 };
var devLogList = [];//设备日志列表，缓存,还不确定这里是否要把所有元素都加进去先。190415
connection.on("onWVResult", function (WVResult) {

 //   document.getElementById("MS_UR").innerText = filedetails.name;
  //  document.getElementById("MS_US").innerText = filedetails.changeType;

    var jsonObj = JSON.parse(WVResult);
    if (myClientID != jsonObj.id || document.getElementById("CreatUser").value != "" || document.getElementById("CreatUser").value != null)
        return;
    document.getElementById("txtBarcode").value = GetTRKCode2(jsonObj.Barcode);
    document.getElementById("Length").value = parseInt(jsonObj.Length, 10);//jsonObj.L as int;
    document.getElementById("Width").value = parseInt(jsonObj.Width, 10);//jsonObj.W;
    document.getElementById("Height").value = parseInt(jsonObj.Height, 10);//jsonObj.H;
    document.getElementById("Weight").value = parseFloat(jsonObj.Weight).toFixed(3);

    let wtVal = parseInt(jsonObj.L, 10) * parseInt(jsonObj.W, 10) * parseInt(jsonObj.H, 10) / 1000 / 5000;
    let wt = parseFloat(jsonObj.weight).toFixed(3);
    document.getElementById("WtVol").value = wtVal.toFixed(3);
    if (wtVal > wt) {
        //     $('#WtVol').col
        $('#WtVol').css('background-color', '#ffff00');
    }
    else
        $('#WtVol').css('background-color', '#ffffff');

    //$("#img").attr("src", "data:image/jpeg;base64,"+jsonObj.picture);
    const base64data = jsonObj.imgbase64Str.replace('data:image/jpeg;base64', '')
        .replace('data:image/png;base64', '');//Strip image type prefix
    //               const buffer = atob(base64data);
    //imgdec.src ="data:image/jpeg;base64,"+jsonObj.picture;
    utils.loadImageToCanvas("data:image/jpeg;base64," + jsonObj.imgbase64Str, 'canvasOutput');
    submitData(1, jsonObj.imgbase64Str);
});