package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreatePostInput;
import evan.ashley.plasma.model.dao.CreatePostOutput;

public interface PostDao {

    CreatePostOutput createPost(CreatePostInput input) throws ValidationException;
}
