(function ($) {

    $(document).ready(function () {
        var locationRoutes = ['/location', '/location/browse', '/location/add'];
        var locationsRegexRoutes = [/\/location\/\d\/details/];

        addClassToNavigationTab(locationRoutes, locationsRegexRoutes);
    });

})(jQuery);