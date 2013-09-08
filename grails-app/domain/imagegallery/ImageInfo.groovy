package imagegallery

class ImageInfo {
	String name
	BigDecimal price
	String url
	
    static constraints = {
    }
	
	String toString() {
		"Image[name=${name}, price=${price}, url=${url}]"
	}
}
