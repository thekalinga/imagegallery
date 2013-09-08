package imagegallery



import org.junit.*
import grails.test.mixin.*

@TestFor(ImageInfoController)
@Mock(ImageInfo)
class ImageInfoControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/imageInfo/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.imageInfoInstanceList.size() == 0
        assert model.imageInfoInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.imageInfoInstance != null
    }

    void testSave() {
        controller.save()

        assert model.imageInfoInstance != null
        assert view == '/imageInfo/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/imageInfo/show/1'
        assert controller.flash.message != null
        assert ImageInfo.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/imageInfo/list'

        populateValidParams(params)
        def imageInfo = new ImageInfo(params)

        assert imageInfo.save() != null

        params.id = imageInfo.id

        def model = controller.show()

        assert model.imageInfoInstance == imageInfo
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/imageInfo/list'

        populateValidParams(params)
        def imageInfo = new ImageInfo(params)

        assert imageInfo.save() != null

        params.id = imageInfo.id

        def model = controller.edit()

        assert model.imageInfoInstance == imageInfo
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/imageInfo/list'

        response.reset()

        populateValidParams(params)
        def imageInfo = new ImageInfo(params)

        assert imageInfo.save() != null

        // test invalid parameters in update
        params.id = imageInfo.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/imageInfo/edit"
        assert model.imageInfoInstance != null

        imageInfo.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/imageInfo/show/$imageInfo.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        imageInfo.clearErrors()

        populateValidParams(params)
        params.id = imageInfo.id
        params.version = -1
        controller.update()

        assert view == "/imageInfo/edit"
        assert model.imageInfoInstance != null
        assert model.imageInfoInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/imageInfo/list'

        response.reset()

        populateValidParams(params)
        def imageInfo = new ImageInfo(params)

        assert imageInfo.save() != null
        assert ImageInfo.count() == 1

        params.id = imageInfo.id

        controller.delete()

        assert ImageInfo.count() == 0
        assert ImageInfo.get(imageInfo.id) == null
        assert response.redirectedUrl == '/imageInfo/list'
    }
}
