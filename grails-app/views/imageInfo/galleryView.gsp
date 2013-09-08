<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8"/>
	<title>Image catalog gallery</title>
	<meta charset="utf-8">

	<meta name="description" content="Buy artistic stock images">
	<meta name="author" content="Anonymous">

	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />

	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

	<link rel="stylesheet" href="${createLink(uri:'/')}css/reveal.min.css">
	<link rel="stylesheet" href="${createLink(uri:'/')}css/theme/default.css" id="theme">
	<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.no-icons.min.css" rel="stylesheet">
	<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
	
	<style>
		#notify-wrapper {
			width: 100%;
			height: 0px;
			left: 0;
			top: 0;
			text-align: center;
			position: fixed;
			margin-top: 3%;
			z-index: 99999;
		}
		
		#controls {
			width: 100%;
			height: 0px;
			bottom: 0;
			right: 0;
			text-align: center;
			position: fixed;
			margin-bottom: 6%;
			z-index: 99999;
		}
		
		#notify {
			-webkit-border-radius: 5px;
			-moz-border-radius: 5px;
			-ms-border-radius: 5px;
			-o-border-radius: 5px;
			border-radius: 5px;
			-moz-box-shadow: 0 1px 2px rgba(0,0,0,0.3),inset 0 0 0 #000;
			-webkit-box-shadow: 0 1px 2px rgba(0,0,0,0.3),inset 0 0 0 #000;
			box-shadow: 0 1px 2px rgba(0,0,0,0.3),inset 0 0 0 #000;
			padding: 6px 10px;
			color: #222;
		}
		
		.success {
			background-color: #c4eca9;
			border: 1px solid #8fdb5c;
		}
		.hidden {
			display:none;
		}
		
		#playPause, #next {
			margin-left: 1.5em;
		}
		
		.jumbotron-icon .icon-2{font-size:2em;}
		.jumbotron-icon .icon-3{font-size:4em;}
		.jumbotron-icon .icon-4{font-size:7em;}
		.jumbotron-icon .icon-5{font-size:12em;}
		.jumbotron-icon .icon-6{font-size:20em;}
		.jumbotron-icon .icon-1,.jumbotron-icon .icon-2,.jumbotron-icon .icon-3,.jumbotron-icon .icon-4,.jumbotron-icon .icon-5,.jumbotron-icon .icon-6{margin-right:0.07142857142857142em;}
	</style>
	<!--[if lt IE 9]>
	<script src="lib/js/html5shiv.js"></script>
	<![endif]-->
</head>
<body>
	<div id="notify-wrapper">
		<span id="notify" style="display:none;" class="success">
			<span id="notify-msg">Test notification</span>
		</span>
	</div>
	<div class="reveal">
		<!-- Any section element inside of this container is displayed as a slide -->
		<div class="slides" id="images">
			<g:each in="${imageInfos}">
				<section id="imageContainer${it.id}">
					<img id="image${it.id}" src="${it.url }"/>
					<p id="name${it.id}">Name: ${it.name }</p>
					<p id="price${it.id}">Price: ${it.price }</p>
				</section>
			</g:each>
		</div>
	</div>
	
	<div id="controls">
		<ul class="list-inline jumbotron-icon">
			<li id="prev" class="btn"><i class="icon-step-backward icon-3"></i></li>
			<li id="playPause" class="btn"><i class="icon-play icon-3"></i></li>
			<li id="next" class="btn"><i class="icon-step-forward icon-3"></i></li>
		</ul>
	</div>
	
	<script src="${createLink(uri:'/')}js/head.min.js"></script>
	<script src="${createLink(uri:'/')}js/reveal.min.js"></script>
	
	<script>

		// Full list of configuration options available here:
		// https://github.com/hakimel/reveal.js#configuration
		Reveal.initialize({
			controls: false,
			progress: false,
			history: true,
			center: true,
			//autoSlide: 2000,
		    loop: true,

			theme: Reveal.getQueryHash().theme, // available themes are in /css/theme
			transition: Reveal.getQueryHash().transition || 'default', // default/cube/page/concave/zoom/linear/fade/none

			// Optional libraries used to extend on reveal.js
			dependencies: [
				{ src: '${createLink(uri:'/')}js/classList.js', condition: function() { return !document.body.classList; } }
			]
		});

	</script>
	
	<script src="${createLink(uri:'/')}js/jquery-1.4.3.min.js"></script>
	<atmosphere:resources />
	<script type="text/javascript">
        $(document).ready(function() {
        		function addNewImage(imgInfo) {
        			var newElemContent = '<section id="imageContainer'+imgInfo.id+'"><img id="image'+imgInfo.id+'" src="'+imgInfo.url+'" alt="" /><p id="name'+imgInfo.id+'">Name: '+imgInfo.name+'</p><p id="price'+imgInfo.id+'">Price: '+imgInfo.price+'</p></section>';
					jQuery('#images').append(newElemContent);
            	}

        		function updateImageDetails(imgInfo) {
					jQuery('#image'+imgInfo.id).attr("src", imgInfo.url);
					jQuery('#name'+imgInfo.id).html("Name : "+imgInfo.name);
					jQuery('#price'+imgInfo.id).html("Price : "+imgInfo.price);
            	}

            	function flashNotifcation(msg) {
            		jQuery("#notify-msg").html(msg);
            		jQuery("#notify").fadeIn(1000).delay(5000).fadeOut(1000);
                }
            	
                // jquery.atmosphere.response
                function callback(response) {
                    if (response.status == 200) {
                        var data = response.responseBody;
                        console.log("response => "+response);
                        if (data.length > 0) {
                            try {
                            	var streamData = jQuery.parseJSON(data)
                            	switch(streamData.updateType) {
                            	case "IMAGE_ADDED":
                            		addNewImage(streamData.imgInfo);
                            		flashNotifcation("Hey, a new image "+streamData.imgInfo.name+" is added to the catalogue");
                                	break;
                            	case "IMAGE_UPDATED":
                            		updateImageDetails(streamData.imgInfo);
                            		flashNotifcation("Hey, the image details have been updated for "+streamData.imgInfo.name);
                                	break;
                            	}
                            } catch (e) {
                                // Atmosphere sends commented out data to WebKit based browsers
                            }
                        }
                    }
                }

                function isPlayInProgress() {
					return (Reveal.getConfig().autoSlide != 0)
                }

                var location = '${createLink(uri:'/')}atmosphere/galleryCatalogue';
                $.atmosphere.subscribe(location, callback, $.atmosphere.request = {transport: 'websocket', fallbackTransport: 'long-polling'});

                jQuery('#prev').click(function() {
                	Reveal.prev();
				});

                jQuery('#playPause').click(function() {
					if(isPlayInProgress()) {
						// Turn autoSlide off
	                	Reveal.configure({ autoSlide: 0 });
	                	jQuery('#playPause :first-child').addClass('icon-play');
                    	jQuery('#playPause :first-child').removeClass('icon-pause');
					} else {
						// Turn autoSlide on
	                	Reveal.configure({ autoSlide: 1000 });
	                	jQuery('#playPause :first-child').addClass('icon-pause');
                    	jQuery('#playPause :first-child').removeClass('icon-play');
					}
				});

                jQuery('#next').click(function() {
                	Reveal.next();
				});
				
        });
	</script>
</body>
</html>