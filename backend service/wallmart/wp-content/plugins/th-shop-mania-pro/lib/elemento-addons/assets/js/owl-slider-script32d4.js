(function ($) {
  function sliderrr(scopeElement, event_) {
    // console.log("event - > ", event_);

    let slider = scopeElement.find(".elemento-owl-slider-common-secript");
    // console.log("slider ->", slider);
    if (slider.length && slider.length > 0) {
      let dataSetting = slider.attr("data-setting");
      if (dataSetting) {
        dataSetting = JSON.parse(dataSetting);
        if (dataSetting) {
          let owlCarouselArg = { slideTransition: "linear", navSpeed: 1000 };
          owlCarouselArg["responsive"] = {
            300: {
              items: dataSetting.items_mobile,
            },
            600: {
              items: dataSetting.items_tablet,
            },
            900: {
              items: dataSetting.items,
            },
          };
          // number of column
          if ("items" in dataSetting) {
            owlCarouselArg["items"] = dataSetting.items;
          }
          //autoplay
          if ("autoplay" in dataSetting) {
            owlCarouselArg["autoplay"] = true;
            owlCarouselArg["autoplaySpeed"] =
              parseInt(dataSetting.autoPlaySpeed) * 1000;
          }
          //dots and navigation speed
          if ("slider_controll" in dataSetting) {
            // for dots
            owlCarouselArg["dots"] =
              dataSetting.slider_controll == "ar_do" ||
              dataSetting.slider_controll == "dot"
                ? true
                : false;
            // for arrows
            owlCarouselArg["nav"] =
              dataSetting.slider_controll == "ar_do" ||
              dataSetting.slider_controll == "arr"
                ? true
                : false;
          }
          // slider loop
          owlCarouselArg["loop"] =
            "slider_loop" in dataSetting && dataSetting.slider_loop == "1"
              ? true
              : false;
          // slider direction
          owlCarouselArg["rtl"] =
            "autoPlayDirection" in dataSetting &&
            dataSetting.autoPlayDirection == "l"
              ? true
              : false;
          //////// lll_lll_yyy_uuu_iii
          let OWlCarouselSlider = slider.find(".elemento-owl-slider");
          // OWlCarouselSlider.data("owlCarousel").destroy();
          var intOWL = OWlCarouselSlider.owlCarousel(owlCarouselArg);
          // if resize container then
          intOWL.trigger("refresh.owl.carousel");

          setTimeout(() => {
            let getSliderHeight = slider.outerHeight();
            let getNavHeight = slider.find(".elemento-addons-owl-np-cln");
            if (getNavHeight && getNavHeight.length) {
              height_ = getNavHeight.outerHeight();
              if (height_) {
                let halfHeight = getSliderHeight / 2;
                // let halfArrowHeight = height_ / 2;
                // let applyTopMargin = halfHeight + halfArrowHeight;
                getNavHeight.css({ top: halfHeight + "px", opacity: 1 });
              }

              // console.log("height_", height_);
            }
          }, 500);
        }
      }
    }
  }


  // list owl product

  $(window).on("elementor/frontend/init", function () {
    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento-product-post.default",
      sliderrr
    );
    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento-carousel-product-list.default",
      sliderrr
    );
    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento-product-category.default",
      sliderrr
    );
    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento-post-layout.default",
      sliderrr
    );
    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento-testimonials.default",
      sliderrr
    );
    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento-product-post-big-product.default",
      sliderrr
    );
    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento-product-simple.default",
      sliderrr
    );

  });

/****************/
// vertical slide
/****************/

$(window).on("elementor/frontend/init", function () {

    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento_product_vertical_list.default",
      function (scopeElement){

        var slidercls = scopeElement.find('.elemento-addons-product-vertical-list-option');
        var rtlv = slidercls.data("rtl");
        var loopv = slidercls.data("loop");
        var autoplayv = slidercls.data("autoplay");
        var autoplayspeedv = slidercls.data("autoplayspeed");
        var autoheightv = slidercls.data("autoheight");
        var navcontroldotv = slidercls.data("navcontroldot");
        var navcontrolarrowv = slidercls.data("navcontrolarrow");
        var animationv = slidercls.data("slideanimation");
        


        if(animationv == 'fadeOut'){

        var animationfadein = "fadeIn";

        var animationfadeout = "fadeOut";

        }else{

        var animationfadein = false;

        var animationfadeout = false;

        }

        var intOWL = scopeElement.find('.elemento-addons-product-vertical-list-slide').owlCarousel({

                rtl:rtlv,

                items:1,

                loop:loopv,

                nav:navcontrolarrowv,

                margin:0,

                autoplay:autoplayv,

                autoplaySpeed:2000,

                autoplayTimeout:autoplayspeedv,

                dots:navcontroldotv,

                autoHeight:autoheightv,

                animateIn:animationfadein, 

                animateOut:animationfadeout,

                navText: ["<i class='th-icon th-icon-arrow-left'></i>",
                       "<i class='th-icon th-icon-arrow-right'></i>"],

            });

            intOWL.trigger("refresh.owl.carousel");

        });
  });

/**********************/
// Advance Product slide
/**********************/

$(window).on("elementor/frontend/init", function () {

    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento_advance_product_slide.default",
      function (scopeElement){

        var slidercls = scopeElement.find('.elemento-addons-adv-product-slide-option');
        var rtlv = slidercls.data("rtl");
        var loopv = slidercls.data("loop");
        var autoplayv = slidercls.data("autoplay");
        var autoplayspeedv = slidercls.data("autoplayspeed");
        var autoheightv = slidercls.data("autoheight");
        var navcontroldotv = slidercls.data("navcontroldot");
        var navcontrolarrowv = slidercls.data("navcontrolarrow");
        var colm = slidercls.data("slidecolm");
        var colmshowtablet = slidercls.data("showtablet");
        var colmshowmobile = slidercls.data("showmobile");

        var gap = slidercls.data("gap") || 15;
        var gaptablet = slidercls.data("gaptablet");
        var gapmobile = slidercls.data("gapmobile");

        var rows = slidercls.data("row");

        var intOWL = scopeElement.find('.elemento-addons-advance-product-list-wrap.owl-carousel').owlCarousel({

                owl2row:rows, 

                rtl:rtlv,

                owl2rowTarget: 'slide',

                items:colm,

                loop:loopv,

                nav:navcontrolarrowv,

                margin:gap,

                autoplay:autoplayv,

                autoplaySpeed:2000,

                autoplayTimeout:autoplayspeedv,

                dots:navcontroldotv,

                autoHeight:autoheightv,

                navText: ["<i class='th-icon th-icon-arrow-left'></i>",
                       "<i class='th-icon th-icon-arrow-right'></i>"],

                responsive:{
                                       0:{
                                           items:colmshowmobile,
                                           margin:gapmobile,
                                       },
                                       768:{
                                           items:colmshowtablet,
                                           margin:gaptablet,
                                       },
                                      1025:{
                                           items:colm,
                                           margin:gap,
                                       },
                             }
                                       

            });

            intOWL.trigger("refresh.owl.carousel");

        });
  });


/****************/
// Woo Coupon
/****************/

$(window).on("elementor/frontend/init", function () {

    elementorFrontend.hooks.addAction(
      "frontend/element_ready/elemento_woo_coupon.default",
      function (scopeElement){

        var slidercls = scopeElement.find('.coupon-slide-option');
        var rtlv = slidercls.data("rtl");
        var loopv = slidercls.data("loop");
        var autoplayv = slidercls.data("autoplay");
        var autoplayspeedv = slidercls.data("autoplayspeed");
        var autoheightv = slidercls.data("autoheight");
        var navcontroldotv = slidercls.data("navcontroldot");
        var navcontrolarrowv = slidercls.data("navcontrolarrow");
        var colm = slidercls.data("slidecolm");
        var colmshowtablet = slidercls.data("showtablet");
        var colmshowmobile = slidercls.data("showmobile");

        var intOWL = scopeElement.find('.elemento-addons-woo-coupon-content-wrap').owlCarousel({

                rtl:rtlv,

                items:2,

                items:colm,

                loop:loopv,

                nav:navcontrolarrowv,

                margin:0,

                autoplay:autoplayv,

                autoplaySpeed:2000,

                autoplayTimeout:autoplayspeedv,

                dots:navcontroldotv,

                autoHeight:autoheightv,

                navText: ["<i class='th-icon th-icon-arrow-left'></i>",
                       "<i class='th-icon th-icon-arrow-right'></i>"],

            });

            intOWL.trigger("refresh.owl.carousel");

        });
  });

})(jQuery);