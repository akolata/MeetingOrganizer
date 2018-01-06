function addClassToNavigationTab(routes) {
    var pathname = window.location.pathname;

    if (isPathnameInProfileRoutes(routes, pathname)) {
        var navigationTabs = $("#navigation-tabs li a");

        navigationTabs.each(function () {
            var currentTab = $(this)[0];

            if (isPathnameInProfileRoutes(routes, currentTab.pathname)) {
                $(currentTab).parent().addClass('active');
            }
        });

    }
}

function isPathnameInProfileRoutes(routes, pathname) {
    return routes.indexOf(pathname) != -1;
}