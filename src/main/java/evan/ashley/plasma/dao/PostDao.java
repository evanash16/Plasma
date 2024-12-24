package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.post.CreatePostInput;
import evan.ashley.plasma.model.dao.post.CreatePostOutput;
import evan.ashley.plasma.model.dao.post.GetPostInput;
import evan.ashley.plasma.model.dao.post.GetPostOutput;

public interface PostDao {

    CreatePostOutput createPost(CreatePostInput input) throws ValidationException;

    GetPostOutput getPost(GetPostInput input) throws ResourceNotFoundException;
}
