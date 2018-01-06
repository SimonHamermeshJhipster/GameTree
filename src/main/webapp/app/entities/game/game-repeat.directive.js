(function() {
    'use strict';

    angular
        .module('gameTreeApp')
        .directive('gameRepeat', ['$timeout',gameRepeat]);

    function gameRepeat($timeout) {

        var directive = {
            restrict: 'E',
            link: linkFunc,
            template:
            '<div class="col-md-8" id=board-{{$index}}></div>'
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var boardId = 'board-' + scope.$index
            $timeout(function(){
                var gameDetail = ChessBoard(boardId, {
                                    draggable: false,
                                    dropOffBoard: 'trash',
                                    sparePieces: false,
                                    position: scope.game.currentState
                                });
            })
        }
    }
})();
