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
        	$("#movie-radio").empty();
            appendElement(data);
        });
}

function appendElement(movieList) {
    for (var i = 0; i < movieList.length; i++) {
        var movie = movieList[i];
        var radio = $("<input type='radio' onclick='showButtons()'>")
        .attr("class", "movie-radio")
        .attr("name", "movie")
        .attr("value", movie.source);
        $("#movie-radio")
        .append(radio);
        
        var movieLink = $("<a>")
        .text(movie.language + " " + movie.quality)
        .attr("href", movie.source)
        .attr("id", movie.id)
        .attr("target", "_blank")
        .append("<br/>");
        $("#movie-radio")
        .append(movieLink);
        
        $("#imdb-id").text(movie.name);
    }
}

function showButtons() {
	$("#cast-button").show();
}

var re = new RegExp("tt[0-9]{7}");

function extractImdbId(value) {
    var matchArr = value.match(re);
    return matchArr[0];
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