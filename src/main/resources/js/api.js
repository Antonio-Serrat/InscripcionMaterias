window.addEventListener('load', function(){
	new Gilder(document.querySelector('.carousel__list'),{
		slidesToShow:1,
		
		dots: '.carousel__index',
		arrows:{
			prev:'.carousel__back',
			next:'.carousel__next'
		}, responsive: [
    {
      // screens greater than >= 775px
      breakpoint: 775,
      settings: {
        // Set to `auto` and provide item width to adjust to viewport
        slidesToShow: 'auto',
        slidesToScroll: 'auto',
        itemWidth: 150,
        duration: 0.25
      }
    },{
      // screens greater than >= 1024px
      breakpoint: 1024,
      settings: {
        slidesToShow: 2,
        slidesToScroll: 1,
        itemWidth: 150,
        duration: 0.25
      }
    }
  ]
});
});