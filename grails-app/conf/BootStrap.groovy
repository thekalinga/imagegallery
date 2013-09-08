import imagegallery.ImageInfo

class BootStrap {

    def init = { servletContext ->
		(1..5).each { index ->
			ImageInfo imgInfo = new ImageInfo(name:index, price:index, url:"http://placehold.it/350&text=${index}")
			imgInfo.save(flush:true, failOnError:true)
			log.debug("Added image #${index}")
		}
    }
    def destroy = {
    }
}
