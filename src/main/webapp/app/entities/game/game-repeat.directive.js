(function() {
    'use strict';

    angular
        .module('gameTreeApp')
        .directive('gameRepeat', ['$timeout',gameRepeat]);

    function gameRepeat($timeout) {

        var directive = {
            restrict: 'E',
            link: linkFunc,
            templateUrl: 'app/entities/game/game-repeat.html'
        };

        return directive;

        function linkFunc(scope, element, attrs) {

            $timeout(function(){
                var gameDetail = ChessBoard('board-' + scope.$index, {
                                    draggable: false,
                                    dropOffBoard: 'trash',
                                    sparePieces: false,
                                    position: scope.game.currentState
                                });
            })
        }
    }
})();
