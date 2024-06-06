package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.category.*;
import team2.elearningapplication.dto.response.admin.category.*;
import team2.elearningapplication.service.ICategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {
    private ICategoryService categoryService;

    @PostMapping("/add-category")
    public ResponseEntity<ResponseCommon<AddCategoryResponse>> addCategory(@Valid @RequestBody AddCategoryRequest addCategoryRequest) {
        ResponseCommon<AddCategoryResponse> response = categoryService.addCategory(addCategoryRequest);
        // if code response equals code Success -> return ok
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } // if code response equals code category exist -> return error
        else if (response.getCode() == ResponseCode.CATEGORY_EXIST.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(ResponseCode.CATEGORY_EXIST.getCode(), "Category already exist", null));
        } // else return fail
        else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "System error", null));
        }
    }

    @PostMapping("/update-category")
    public ResponseEntity<ResponseCommon<UpdateCategoryResponse>> updateCategory(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        ResponseCommon<UpdateCategoryResponse> response = categoryService.updateCategory(updateCategoryRequest);
        // if code response equals code succes -> return ok
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } // if code response equals code category not exist -> return error
        else if (response.getCode() == ResponseCode.CATEGORY_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST.getCode(), "Category not exist", null));
        } // else - return fail
        else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update Fail", null));

        }
    }

    @PostMapping("/delete-category")
    public ResponseEntity<ResponseCommon<DeleteCategoryResponse>> deleteCategory(@Valid @RequestBody DeleteCategoryRequest deleteCategoryRequest) {
        ResponseCommon<DeleteCategoryResponse> response = categoryService.deleteCategory(deleteCategoryRequest);
        // if code response equals code succes -> return ok
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } // if code response equals code category not exist -> return error
        else if (response.getCode() == ResponseCode.CATEGORY_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST.getCode(), "Category not exist", null));
        } // else - return fail
        else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete Fail", null));
        }
    }

    @GetMapping("/find-all-category")
    public ResponseEntity<ResponseCommon<FindAllCategoryResponse>> findAllCategory() {
        ResponseCommon<FindAllCategoryResponse> response = categoryService.findAllCategory();
        // if code response equals code success -> return ok
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.CATEGORY_LIST_IS_EMPTY.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.CATEGORY_LIST_IS_EMPTY.getCode(), "List Category is Empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find All category fail", null));
        }
    }

    @GetMapping("/get-category-by-id")
    public ResponseEntity<ResponseCommon<GetCategoryByIdResponse>> getCategoryById(GetCategoryByIdRequest request) {
        ResponseCommon<GetCategoryByIdResponse> response = categoryService.getCategoryBYId(request);
        // if code response equals code success -> return ok
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.CATEGORY_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST.getCode(), "List Category is Empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get category by id fail", null));
        }
    }

    @GetMapping("/find-all-category-by-deleted")
    public ResponseEntity<ResponseCommon<FindAllCategoryResponse>> findAllCategoryByDeleted(GetCategoryByDeletedRequest findAllAnswerByDeletedRequest) {
        ResponseCommon<FindAllCategoryResponse> response = categoryService.getCategoryByDeleted(findAllAnswerByDeletedRequest);
        // if code response equals code success -> return ok
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.CATEGORY_LIST_IS_EMPTY.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.CATEGORY_LIST_IS_EMPTY.getCode(), "List Category is Empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find All category fail", null));
        }
    }
}
