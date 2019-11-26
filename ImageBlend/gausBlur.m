% gausBlur function
% input: image descriptor im
% output: blurred image descriptor smooxy
function [smoox,smooxy] = gausBlur(im)
    % simple approximation to a 1D Gaussian filter with sigma=1
    gau = [1 4 6 4 1]/16;
    % blur along rows
    smoox = imfilter(im,gau,'conv','same','replicate');
    % then blur along cols
    smooxy = imfilter(smoox,gau','conv','same','replicate');
end