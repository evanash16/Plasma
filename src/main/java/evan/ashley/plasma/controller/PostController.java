package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.PostDao;
import evan.ashley.plasma.model.api.post.CreatePostRequest;
import evan.ashley.plasma.model.api.post.CreatePostResponse;
import evan.ashley.plasma.model.api.post.GetPostResponse;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.api.post.ImmutableCreatePostResponse;
import evan.ashley.plasma.model.api.post.ImmutableGetPostResponse;
import evan.ashley.plasma.model.dao.post.CreatePostOutput;
import evan.ashley.plasma.model.dao.post.GetPostOutput;
import evan.ashley.plasma.model.dao.post.ImmutableCreatePostInput;
import evan.ashley.plasma.model.dao.post.ImmutableGetPostInput;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class PostController {

    @Autowired
    private PostDao postDao;

    @PostMapping("/post")
    public CreatePostResponse createPost(@RequestBody final CreatePostRequest request) throws ValidationException {
        final CreatePostOutput output = postDao.createPost(ImmutableCreatePostInput.builder()
                .postedById(request.getPostedById())
                .title(request.getTitle())
                .body(request.getBody())
                .build());
        return ImmutableCreatePostResponse.builder()
                .id(output.getId())
                .build();
    }

    @GetMapping("/post/{id}")
    public GetPostResponse getPost(@PathVariable("id") final String id) throws ResourceNotFoundException {
        final GetPostOutput output = postDao.getPost(ImmutableGetPostInput.builder()
                .id(id)
                .build());
        return ImmutableGetPostResponse.builder()
                .id(output.getId())
                .postedById(output.getPostedById())
                .creationTime(output.getCreationTime())
                .lastModificationTime(output.getLastModificationTime())
                .title(output.getTitle())
                .body(output.getBody())
                .build();
    }
}
