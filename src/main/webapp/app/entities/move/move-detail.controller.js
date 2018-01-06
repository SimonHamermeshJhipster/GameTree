(function() {
    'use strict';

    angular
        .module('gameTreeApp')
        .controller('MoveDetailController', MoveDetailController);

    MoveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Move', 'User', 'Game'];

    function MoveDetailController($scope, $rootScope, $stateParams, previousState, entity, Move, User, Game) {
        var vm = this;

        vm.move = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameTreeApp:moveUpdate', function(event, result) {
            vm.move = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
