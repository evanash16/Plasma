package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreatePostInput;
import evan.ashley.plasma.model.dao.CreatePostOutput;
import evan.ashley.plasma.model.dao.GetPostInput;
import evan.ashley.plasma.model.dao.GetPostOutput;

public interface PostDao {

    CreatePostOutput createPost(CreatePostInput input) throws ValidationException;

    GetPostOutput getPost(GetPostInput input) throws ResourceNotFoundException;
}
