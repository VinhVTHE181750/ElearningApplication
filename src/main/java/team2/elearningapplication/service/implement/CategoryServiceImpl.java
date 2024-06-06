package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.category.*;
import team2.elearningapplication.dto.response.admin.category.*;
import team2.elearningapplication.entity.Category;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.ICategoryRepository;
import team2.elearningapplication.repository.IUserRepository;
import team2.elearningapplication.service.ICategoryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;

    @Override
    public ResponseCommon<AddCategoryResponse> addCategory(AddCategoryRequest addCategoryRequest) {
        try {
            Category category = categoryRepository.findCategoryByName(addCategoryRequest.getName()).orElse(null);
            User user = userRepository.findByUsername(addCategoryRequest.getUsername()).orElse(null);
            // if category not null -> tell user
            if (!Objects.isNull(category)) {
                log.debug("Add Category failed: Category already exists");
                return new ResponseCommon<>(ResponseCode.CATEGORY_EXIST, null);
            }
            // if category is null -> new category
            if (Objects.isNull(category)) {
                category = new Category();
            }

            category.setName(addCategoryRequest.getName());
            category.setUserCreated(user);
            // Save category to the database
            Category savedCategory = categoryRepository.save(category);

            // If category is not saved successfully, return a FAIL response
            if (savedCategory == null) {
                log.debug("Add Category failed: Unable to save the category");
                return new ResponseCommon<>(ResponseCode.FAIL, null);
            }

            log.debug("Add Category successful");
            AddCategoryResponse addCategoryResponse = new AddCategoryResponse();
            addCategoryResponse.setCategoryID(category.getId());
            addCategoryResponse.setCategoryName(category.getName());
            return new ResponseCommon<>(ResponseCode.SUCCESS, addCategoryResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Add Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<UpdateCategoryResponse> updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        try {
            Category category = categoryRepository.findCategoryById(updateCategoryRequest.getCategoryID()).orElse(null);
            User user = userRepository.findByUsername(updateCategoryRequest.getUsername()).orElse(null);
            // if category is null -> tell user
            if (Objects.isNull(category)) {
                log.debug("Update Category failed: Category does not exist");
                return new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST, null);
            } else {
                Category categoryUpdate = category;
                categoryUpdate.setName(updateCategoryRequest.getCategoryUpdate());
                categoryUpdate.setUpdatedAt(LocalDateTime.now());
                categoryUpdate.setDeleted(updateCategoryRequest.isDeleted());
                categoryUpdate.setUserUpdated(user);
                categoryRepository.save(categoryUpdate);
                UpdateCategoryResponse updateCategoryResponse = new UpdateCategoryResponse();
                updateCategoryResponse.setCategoryID(categoryUpdate.getId());
                updateCategoryResponse.setCategoryName(categoryUpdate.getName());
                log.debug("Update Category successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, updateCategoryResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Update Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<DeleteCategoryResponse> deleteCategory(DeleteCategoryRequest deleteCategoryRequest) {
        try {
            Category category = categoryRepository.findCategoryById(deleteCategoryRequest.getCategoryID()).orElse(null);
            User user = userRepository.findByUsername(deleteCategoryRequest.getUsername()).orElse(null);
            // if category is null -> tell the user
            if (Objects.isNull(category)) {
                log.debug("Delete Category failed: Category does not exist");
                return new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST, null);
            } else {
                Category categoryUpdate = category;
                categoryUpdate.setDeleted(true);
                categoryUpdate.setUpdatedAt(LocalDateTime.now());
                categoryUpdate.setUserUpdated(user);
                categoryRepository.save(categoryUpdate);
                DeleteCategoryResponse deleteCategoryResponse = new DeleteCategoryResponse();
                deleteCategoryResponse.setCategoryID(categoryUpdate.getId());
                deleteCategoryResponse.setCategoryName(categoryUpdate.getName());
                deleteCategoryResponse.setDeleted(categoryUpdate.isDeleted());
                log.debug("Delete Category successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, deleteCategoryResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Delete Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<FindAllCategoryResponse> findAllCategory() {
        try {
            List<Category> listCategory = categoryRepository.findAllByIsDeleted(false);
            // if the list is empty -> tell the user
            if (listCategory.isEmpty()) {
                log.debug("Get all Category failed: Category list is empty");
                return new ResponseCommon<>(ResponseCode.CATEGORY_LIST_IS_EMPTY, null);
            } else {
                FindAllCategoryResponse response = new FindAllCategoryResponse("Get all success", listCategory);
                log.debug("Get all Category successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get all Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetCategoryByIdResponse> getCategoryBYId(GetCategoryByIdRequest getCategoryByIdRequest) {
        try {
            Category category = categoryRepository.findCategoryById(getCategoryByIdRequest.getId()).orElse(null);
            // if category is null -> tell the user
            if (Objects.isNull(category)) {
                log.debug("Get Category by id failed: Category does not exist");
                return new ResponseCommon<>(ResponseCode.CATEGORY_NOT_EXIST, null);
            } else {
                GetCategoryByIdResponse response = new GetCategoryByIdResponse();
                response.setId(category.getId());
                response.setName(category.getName());
                response.setDeleted(category.isDeleted());

                log.debug("Get Category By id successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Category by id failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<FindAllCategoryResponse> getCategoryByDeleted(GetCategoryByDeletedRequest getCategoryByDeletedRequest) {
        try {
            List<Category> listCategory = categoryRepository.findAllByIsDeleted(getCategoryByDeletedRequest.isDeleted());
            // if the list is empty -> tell the user
            if (listCategory.isEmpty()) {
                log.debug("Get all Category failed: Category list is empty");
                return new ResponseCommon<>(ResponseCode.CATEGORY_LIST_IS_EMPTY, null);
            } else {
                FindAllCategoryResponse response = new FindAllCategoryResponse("Get all success", listCategory);
                log.debug("Get all Category successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get all Category failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }
}
