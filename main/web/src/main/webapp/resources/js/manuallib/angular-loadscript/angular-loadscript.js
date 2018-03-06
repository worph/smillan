(function () {
    'use strict';

    var mapTemplate = angular.module('script-lazy-directive', []);

    mapTemplate.directive('script-lazy', function () {
        return {
            restrict: 'E',
            scope: false,
            link: function (scope, elem, attr)
            {
                alert('lazy');
                var s = document.createElement("script");
                s.type = "text/javascript";
                var src = elem.attr('src');
                if (src !== undefined)
                {
                    s.src = src;
                } else
                {
                    var code = elem.text();
                    s.text = code;
                }
                document.head.appendChild(s);
                elem.remove();
            }
        };
    });

})();

app.directive('script', function () {
    return {
        restrict: 'E',
        scope: false,
        link: function (scope, elem, attr)
        {
            if (attr.type === 'text/javascript-lazy')
            {
                alert('alzy');
                var s = document.createElement("script");
                s.type = "text/javascript";
                var src = elem.attr('src');
                if (src !== undefined)
                {
                    s.src = src;
                } else
                {
                    var code = elem.text();
                    s.text = code;
                }
                document.head.appendChild(s);
                elem.remove();
            }
        }
    };
});


