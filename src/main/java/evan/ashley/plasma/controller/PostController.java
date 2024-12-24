package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.PostDao;
import evan.ashley.plasma.model.api.CreatePostRequest;
import evan.ashley.plasma.model.api.CreatePostResponse;
import evan.ashley.plasma.model.api.ImmutableCreatePostResponse;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreatePostOutput;
import evan.ashley.plasma.model.dao.ImmutableCreatePostInput;
import evan.ashley.plasma.model.db.Post;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
}
