package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.PostDao;
import evan.ashley.plasma.model.api.CreatePostRequest;
import evan.ashley.plasma.model.api.CreatePostResponse;
import evan.ashley.plasma.model.api.GetPostResponse;
import evan.ashley.plasma.model.api.ImmutableCreatePostResponse;
import evan.ashley.plasma.model.api.ImmutableGetPostResponse;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreatePostOutput;
import evan.ashley.plasma.model.dao.GetPostOutput;
import evan.ashley.plasma.model.dao.ImmutableCreatePostInput;
import evan.ashley.plasma.model.dao.ImmutableGetPostInput;
import evan.ashley.plasma.model.db.Post;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
