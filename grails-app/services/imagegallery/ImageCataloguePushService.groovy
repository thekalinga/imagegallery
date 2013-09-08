package imagegallery

import grails.converters.JSON

import org.atmosphere.cpr.AtmosphereResourceEvent

class ImageCataloguePushService {

	static transactional = false

	static atmosphere = [mapping: '/atmosphere/galleryCatalogue']
	
	def onRequest = { event ->
		// We should only have GET requests here
		log.info "onRequest, method: ${event.request.method}"

		// Mark this connection as suspended.
		event.suspend()
    }

    def onStateChange = { event ->
		if (event.message) {
			log.info "onStateChange, message: ${event.message}"
			def underlygingMsg = event.message
			def imgInfo = ImageInfo.get(underlygingMsg.id)
			switch(underlygingMsg.type) {
				case 'IMAGE_ADDED':
					log.info("New image ${imgInfo} added.")
					break;
				case 'IMAGE_UPDATED':
					log.info("Image ${imgInfo} updated.")
					break;
			}
		
			def notficationData = [updateType:underlygingMsg.type, imgInfo:imgInfo]
			
			if (event.isSuspended()) {
				event.resource.response.writer.with {
					write "${notficationData as JSON}"
					//write "<script>parent.callback('${ImageInfo.get(underlygingMsg.id) as JSON}');</script>"
					flush()
				}
				event.resume()
			}
//		if (event.message) {
//			log.info "onStateChange, message: ${event.message}"
//
//			if (event.isSuspended()) {
//				event.resource.response.writer.with {
//					write "<script>parent.callback('${event.message}');</script>"
//					flush()
//				}
//				event.resume()
//			}
		}
    }

}
