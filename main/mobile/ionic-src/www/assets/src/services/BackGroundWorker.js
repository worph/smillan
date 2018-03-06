(function () {
    'use strict';

    var users = angular.module('module.background.worker', []);

    users.service('BackGroundWorker', [
        '$log',
        '$q',
        function (
                $log, $q) {

            var BackgroundWorker = function (maxTasks) {
                this.maxTasks = maxTasks || 100;
                this.runningTasks = 0;
                this.taskQueue = [];
            };


            BackgroundWorker.prototype.cancelAllTask = function () {
                this.taskQueue = [];
            };

            /* runs an async task => function(resolve,fail) */
            BackgroundWorker.prototype.runTask = function (task) {
                var resolveFunction;
                var failFunction;
                var future = $q(function (resolve, fail) {
                    resolveFunction = resolve;
                    failFunction=fail;
                });
                var self = this;
                if (self.runningTasks >= self.maxTasks) {
                    self.taskQueue.push({task: task, resolveFunction: resolveFunction,failFunction:failFunction});
                } else {
                    executeTask(self, task, resolveFunction,failFunction);
                }
                return future;
            };

            function executeTask(bwobject, task, resolve,fail) {
                bwobject.runningTasks += 1;
                var promise = $q(task);
                promise.then(function () {
                    resolve();
                    taskCompleted(bwobject);
                }, function (err) {
                    $log.error(err);
                    fail(err);
                    taskCompleted(bwobject);//that way we do not break the pipe
                });
            }

            function taskCompleted(bwobject) {
                bwobject.runningTasks -= 1;

                // are any tasks waiting in queue?
                if (bwobject.taskQueue.length > 0) {
                    // it seems so! let's run it x)
                    var taskInfo = bwobject.taskQueue.splice(0, 1)[0];
                    executeTask(bwobject,taskInfo.task,taskInfo.resolveFunction,taskInfo.failFunction);
                }
            };

            return {
                createWorker: function (maxExec) {
                    return new BackgroundWorker(maxExec);
                }
            };
        }
    ]);

})();