
onmessage = function(e) {
  console.log('ExecuteTask');
  var workerResult = 'Result: ' +e.data[0] +"/"+ e.data[1];
  postMessage(workerResult);
}
