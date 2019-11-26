% CMPEN-454 Project 1
% Image Blending Application

% read in cameraman images
im = imread('cameraman.jpg');
im2 = imread('cameraman2.jpg');
% downsample threshold is 10 pixels
k = 10;

% Building Gaussian and Laplacian pyramids
% from two different images
% downsample threshold is 10 pixels
[gaussPyra1, lapPyra1] = buildPyra(im, k, 0);
[gaussPyra2, lapPyra2] = buildPyra(im2, k, 0);

% if you want to use your own filter
% uncomment this line
% roipoly gives you a GUI selector
%BW = roipoly(im);

% if not, default filter is defined here
% assuming both images are of same size
% default filter keeps left and right half of each image
xsize = length(im(:,1,1));
ysize = length(im(1,:,1));
halfysize = ceil(ysize/2);
lefthalf = zeros(xsize, halfysize, 3, 'uint8');
righthalf = ones(xsize, ysize-halfysize, 3, 'uint8');
BW = double([lefthalf righthalf]);
[filterGausPyra,~] = buildPyra(BW, k, 1);

n = length(filterGausPyra);
blendedG = cell(n);
blendedL = cell(n-1);
% create blended Gaussian and Laplacian Pyramid
for i = 1:n
    blendedG{i} = double(gaussPyra1{i}) .* (1-filterGausPyra{i}) + ...
        double(gaussPyra2{i}) .* filterGausPyra{i};
    if i ~= n
        blendedL{i} = double(lapPyra1{i}) .* (1-filterGausPyra{i}) + ...
            double(lapPyra2{i}) .* filterGausPyra{i};
    end
end
result = recoverImg(blendedG{n},blendedL);