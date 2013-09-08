package imagegallery

class ImageInfoController {
	static scaffold = true
	
	def save() {
		def imageInfoInstance = new ImageInfo(params)
		if (!imageInfoInstance.save(flush: true)) {
			render(view: "create", model: [imageInfoInstance: imageInfoInstance])
			return
		}

		broadcaster['/atmosphere/galleryCatalogue'].broadcast([type:"IMAGE_ADDED", id:imageInfoInstance.id])
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'imageInfo.label', default: 'imageInfo'), imageInfoInstance.id])
		redirect(action: "show", id: imageInfoInstance.id)
	}
	
	def update(Long id, Long version) {
		def imageInfoInstance = ImageInfo.get(id)
		if (!imageInfoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'imageInfo.label', default: 'imageInfo'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (imageInfoInstance.version > version) {
				imageInfoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'imageInfo.label', default: 'imageInfo')] as Object[],
						  "Another user has updated this imageInfo while you were editing")
				render(view: "edit", model: [imageInfoInstance: imageInfoInstance])
				return
			}
		}

		imageInfoInstance.properties = params

		if (!imageInfoInstance.save(flush: true)) {
			render(view: "edit", model: [imageInfoInstance: imageInfoInstance])
			return
		}
		
		broadcaster['/atmosphere/galleryCatalogue'].broadcast([type:"IMAGE_UPDATED", id:imageInfoInstance.id])

		flash.message = message(code: 'default.updated.message', args: [message(code: 'imageInfo.label', default: 'imageInfo'), imageInfoInstance.id])
		redirect(action: "show", id: imageInfoInstance.id)
	}
	
	def addTest() {
		broadcaster['/atmosphere/galleryCatalogue'].broadcast([type:"IMAGE_ADDED", id:1])
		render("Hello")
	}
	
	def updateTest() {
		broadcaster['/atmosphere/galleryCatalogue'].broadcast([type:"IMAGE_UPDATED", id:1])
	}
	
	def galleryView() {
		[imageInfos : ImageInfo.list()]
	}
}
