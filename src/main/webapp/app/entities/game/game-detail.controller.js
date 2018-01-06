(function() {
    'use strict';

    angular
        .module('gameTreeApp')
        .controller('GameDetailController', GameDetailController);

    GameDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Game', 'Move', 'User'];

    function GameDetailController($scope, $rootScope, $stateParams, previousState, entity, Game, Move, User) {
        var vm = this;

        vm.game = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameTreeApp:gameUpdate', function(event, result) {
            vm.game = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
