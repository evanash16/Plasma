package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.post.CreatePostInput;
import evan.ashley.plasma.model.dao.post.CreatePostOutput;
import evan.ashley.plasma.model.dao.post.DeletePostInput;
import evan.ashley.plasma.model.dao.post.GetPostInput;
import evan.ashley.plasma.model.dao.post.GetPostOutput;
import evan.ashley.plasma.model.dao.post.ListPostsInput;
import evan.ashley.plasma.model.dao.post.ListPostsOutput;
import evan.ashley.plasma.model.dao.post.UpdatePostInput;

public interface PostDao {

    CreatePostOutput createPost(CreatePostInput input) throws ValidationException;

    void updatePost(UpdatePostInput input) throws ResourceNotFoundException;

    void deletePost(DeletePostInput input) throws ResourceNotFoundException;

    GetPostOutput getPost(GetPostInput input) throws ResourceNotFoundException;

    ListPostsOutput listPosts(ListPostsInput input) throws ValidationException;
}
