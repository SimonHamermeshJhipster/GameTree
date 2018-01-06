(function() {
    'use strict';

    angular
        .module('gameTreeApp')
        .controller('MoveController', MoveController);

    MoveController.$inject = ['Move'];

    function MoveController(Move) {

        var vm = this;

        vm.moves = [];

        loadAll();

        function loadAll() {
            Move.query(function(result) {
                vm.moves = result;
                vm.searchQuery = null;
            });
        }
    }
})();
