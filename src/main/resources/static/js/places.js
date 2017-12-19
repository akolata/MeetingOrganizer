(function ($) {

    var placesRoutes = ['/places', '/places/browse', '/places/add'];

    function addClassToNavigationTab() {
        var pathname = window.location.pathname;

        if (isPathnameInProfileRoutes(pathname)) {
            var navigationTabs = $("#navigation-tabs li a");

            navigationTabs.each(function () {
                var currentTab = $(this)[0];

                if (isPathnameInProfileRoutes(currentTab.pathname)) {
                    $(currentTab).parent().addClass('active');
                }
            });

        }
    }

    function isPathnameInProfileRoutes(pathname) {
        return placesRoutes.indexOf(pathname) != -1;
    }

    addClassToNavigationTab();
})(jQuery);