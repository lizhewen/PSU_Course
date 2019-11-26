% buildPyra function returning image's Gaussian and Laplacian pyramids
% input: im - image descriptor
% input: k - threshold pixel to stop downsampling
% input: option 1 for generating Gaussian Pyramid only
% output: gaussPyra - cell array for Gaussian Pyramid
% output: lapPyra - cell array for Laplacian Pyramid
function [gaussPyra, lapPyra] = buildPyra(im,k,option)
    % number of pixels in x-direction for the original pic
    originalsize = length(im(:,1,1));
    % calculating downsampling times
    n = ceil(log2(originalsize/k));
    % initialize arrays to store image layers
    gaussPyra = cell(3*n);
    lapPyra = cell(3*n-1);
    % generating gaussian pyramids
    for i = 1:3:3*n
        % the first layer is orinigal image
        if i == 1
            gaussPyra{i} = im;
        else
        % downsample image once
            gaussPyra{i} = downsample(gaussPyra{i-1});
        end
        % using gaussian filter to blur the image
        [gaussPyra{i+1},gaussPyra{i+2}] = gausBlur(gaussPyra{i});
    end
    % generating laplacian pyramids
    for i = 1:3*n-1
        % option 1 for generating Gaussian Pyramid only
        if option == 1
            break
        end
        % difference across different size of imgs
        if rem(i,3) == 0
            % upsample {i+1}th layer
            upsamplexy = upsample(gaussPyra{i+1});
            lapPyra{i} = gaussPyra{i} - upsamplexy;
        else
            lapPyra{i} = gaussPyra{i} - gaussPyra{i+1};
        end
    end
end