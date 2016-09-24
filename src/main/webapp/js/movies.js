var serviceUri = "webapi/movies";

function doSearch(imdbId) {
    $.ajax({
        url: serviceUri + "/" + imdbId,
        type: 'GET',
        contentType: 'application/json',
        beforeSend: function(){
            $(".loader").css("animation-iteration-count", "infinite");
            $(".loader").css("display", "inline");
        },
        complete: function(){
            $(".loader").css("animation-iteration-count", 0);
            $(".loader").css("display", "none");
        }
    })
        .done(function (data, status, xhr) {
            appendElement(data);
        });
}

function appendElement(movieList) {
	var radio = "";
    for (var i = 0; i < movieList.length; i++) {
        var movie = movieList[i];
        radio += "<label><input type='radio' onclick='showButtons()' name='movie' value='" + movie.source + "'>" + movie.language + " " + movie.quality +"</input></label><br/>";
        var li = "<li><a href='" +  + "' id='"+movie.id+"' target='_blank'>"+movie.language + " " + movie.quality+"</a>";
        $("#movie-radio")
        .append(radio)
    }
    
}

function showButtons() {
	$("#cast-button").show();
    $("#play-button").show();
}

var re = new RegExp("tt[0-9]{7}");

function extractImdbId(value) {
    var matchArr = value.match(re);
    return matchArr[0];
}

function openChromecastPanel() {
	var movie = $('input[name=movie]:checked').val();
	$("#url").val(movie);
	$("#cast").show();
}

function playMovie() {
	window.open($('input[name=movie]:checked').val());
}

///+++ on load +++

$(function () {

    $("#search-button").click(
        function () {
            $("#movie-list").empty();
            var value = $("#text-input").val();
            value = extractImdbId(value);
            $("#imdb-id").text(value);
            doSearch(value);
        });

});