(function() {
    'use strict';
    angular
        .module('gameTreeApp')
        .factory('Game', Game);

    Game.$inject = ['$resource', 'DateUtils'];

    function Game ($resource, DateUtils) {
        var resourceUrl =  'api/games/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timeUpdated = DateUtils.convertLocalDateFromServer(data.timeUpdated);
                        data.timeCreated = DateUtils.convertLocalDateFromServer(data.timeCreated);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.timeUpdated = DateUtils.convertLocalDateToServer(copy.timeUpdated);
                    copy.timeCreated = DateUtils.convertLocalDateToServer(copy.timeCreated);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.timeUpdated = DateUtils.convertLocalDateToServer(copy.timeUpdated);
                    copy.timeCreated = DateUtils.convertLocalDateToServer(copy.timeCreated);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
