(function ($) {

    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if(results) {
            return results[1];
        }
    };

    $(document).ready(function () {

        $('#page-size-select').on('change', function() {
           var selectedPageSize = this.value;
           var windowUrl = window.location.href;
           var contextUrl = windowUrl.split('?')[0];
           var page = $.urlParam('page') || '1';


           window.location.href = contextUrl + '?page=' + page + '&pageSize=' + selectedPageSize;
        });
    });

})(jQuery);