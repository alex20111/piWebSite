$(function() {

    var chart = Morris.Line({
        element: 'morris-area-chart',
        data: [{
            period: '2013-03-30 22:00:00',
            iphone: 2666,
            ipad: null,
            itouch: 2647
        }, {
            period: '2013-03-31 00:00:00',
            iphone: 2778,
            ipad: 2294,
            itouch: 2441
        }, {
            period: '2013-03-31 02:00:00',
            iphone: 4912,
            ipad: 1969,
            itouch: 2501
        }, {
            period: '2013-03-31 04:00:00',
            iphone: 3767,
            ipad: 3597,
            itouch: 5689
        }, {
            period: '2013-03-32 04:00:00',
            iphone: 3767,
            ipad: 3597,
            itouch: 5689
        }, {
            period: '2013-03-32 06:00:00',
            iphone: 3767,
            ipad: 3597,
            itouch: 5689
        }],
        xkey: 'period',
        ykeys: ['iphone', 'ipad', 'itouch'],
        labels: ['iPhone', 'iPad', 'iPod Touch'],
        pointSize: 2,
        hideHover: 'auto',
        resize: true
    });


   chart.options.labels.forEach(function(label, i) {
    var legendItem = $('<span></span>').text( label ).prepend('<span>&nbsp;</span>');
    legendItem.find('span')
     .css('backgroundColor', chart.options.lineColors[i])
      .css('width', '18px')
      .css('display', 'inline-block')
      .css('margin', '2px');
    $('#legend').append(legendItem);
  }); 
    
    
});
